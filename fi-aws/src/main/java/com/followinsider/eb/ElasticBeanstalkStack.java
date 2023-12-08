package com.followinsider.eb;

import software.amazon.awscdk.Stack;
import software.amazon.awscdk.services.elasticbeanstalk.*;
import software.amazon.awscdk.services.iam.*;
import software.amazon.awscdk.services.s3.assets.Asset;
import software.amazon.awscdk.services.s3.assets.AssetProps;
import software.amazon.awscdk.services.ec2.*;
import software.constructs.Construct;

import java.util.List;
import java.util.stream.Collectors;

public class ElasticBeanstalkStack extends Stack {

    public ElasticBeanstalkStack(Construct scope, ElasticBeanstalkProps props) {
        super(scope, props.getAlias() + "-eb-stack", props.getStackProps());
        String alias = props.getAlias();

        // ElasticBeanStalk application
        var app = new CfnApplication(this, alias + "-app",
                CfnApplicationProps.builder()
                        .applicationName(props.getAppName())
                        .build());

        // S3 asset with the JAR file
        var asset = new Asset(this, alias + "-asset",
                AssetProps.builder()
                        .path(props.getAssetPath())
                        .build());

        var sourceBundle = CfnApplicationVersion.SourceBundleProperty.builder()
                .s3Bucket(asset.getS3BucketName())
                .s3Key(asset.getS3ObjectKey())
                .build();

        // App version from the S3 asset defined earlier
        var version = new CfnApplicationVersion(this, props.getAlias() + "-version",
                CfnApplicationVersionProps.builder()
                        .applicationName(props.getAppName())
                        .sourceBundle(sourceBundle)
                        .build());

        // Make sure that the app exists before creating its version
        version.getNode().addDependency(app);

        // IAM role and instance profile
        var role = new Role(this, alias + "-iam-role",
                RoleProps.builder()
                        .assumedBy(new ServicePrincipal("ec2.amazonaws.com"))
                        .build());

        IManagedPolicy policy = ManagedPolicy.fromAwsManagedPolicyName("AWSElasticBeanstalkWebTier");
        role.addManagedPolicy(policy);

        String profileName = props.getAlias() + "-iam-profile";
        var profile = new CfnInstanceProfile(this, profileName,
                CfnInstanceProfileProps.builder()
                        .instanceProfileName(profileName)
                        .roles(List.of(role.getRoleName()))
                        .build());

        // Default VPC with public subnets
        var vpc = Vpc.fromLookup(this, "default-vpc",
                VpcLookupOptions.builder()
                        .isDefault(true)
                        .build());

        String publicSubnets = vpc.getPublicSubnets().stream()
                .map(ISubnet::getSubnetId)
                .collect(Collectors.joining(","));

        // Option settings of application environment
        boolean https = props.isHttps();
        String certificateArn = System.getenv("CERTIFICATE_ARN");
        List<CfnEnvironment.OptionSettingProperty> settings = new CfnSettingsBuilder()

                /* Elastic Beanstalk */
                .setting("aws:elasticbeanstalk:application:environment", "SERVER_PORT", props.getPort())
                .setting("aws:elasticbeanstalk:environment:process:default", "HealthCheckPath", props.getHealthUrl())
                    .setting("aws:elasticbeanstalk:environment", "EnvironmentType", "LoadBalanced")
                .setting("aws:elasticbeanstalk:environment", "LoadBalancerType", "application")

                /* Load balancer for HTTPS */
                .settingIf(https, "aws:elbv2:listener:443", "SSLCertificateArns", certificateArn)
                .settingIf(https, "aws:elbv2:listener:443", "ListenerEnabled", "true")
                .settingIf(https, "aws:elbv2:listener:443", "Protocol", "HTTPS")

                /* Load balancer for HTTP */
                .settingIf(!https, "aws:elbv2:listener:80", "ListenerEnabled", "true")
                .settingIf(!https, "aws:elbv2:listener:80", "Protocol", "HTTP")

                /* Autoscaling */
                .setting("aws:autoscaling:launchconfiguration", "IamInstanceProfile", profile.getRef())
                .setting("aws:autoscaling:asg", "MinSize", "1")
                .setting("aws:autoscaling:asg", "MaxSize", "1")

                /* EC2 */
                .setting("aws:ec2:instances", "InstanceTypes", props.getInstanceType())
                .setting("aws:ec2:vpc", "Subnets", publicSubnets)
                .setting("aws:ec2:vpc", "VPCId", vpc.getVpcId())

                .build();

        // Elastic Beanstalk environment to run the application
        String envName = alias + "-eb-env";
        new CfnEnvironment(this, envName,
                CfnEnvironmentProps.builder()
                        .applicationName(app.getApplicationName())
                        .environmentName(envName)
                        .solutionStackName(props.getStackName())
                        .optionSettings(settings)
                        .versionLabel(version.getRef())
                        .build());
    }

}
