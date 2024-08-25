package com.followinsider;

import software.amazon.awscdk.Environment;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
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
        super(scope, props.getAppName() + "-eb-stack", buildStackProps(props));

        String name = props.getAppName();

        // Cloud Formation application
        var appProps = CfnApplicationProps.builder()
                .applicationName(name)
                .build();

        var app = new CfnApplication(this, name + "-eb-app", appProps);

        // Cloud Formation asset (from the JAR file)
        var assetProps = AssetProps.builder()
                .path(props.getAssetPath())
                .build();

        var asset = new Asset(this, name + "-asset", assetProps);

        var sourceBundle = CfnApplicationVersion.SourceBundleProperty.builder()
                .s3Bucket(asset.getS3BucketName())
                .s3Key(asset.getS3ObjectKey())
                .build();

        // Cloud Formation application version
        var versionProps = CfnApplicationVersionProps.builder()
                .applicationName(name)
                .sourceBundle(sourceBundle)
                .build();

        var version = new CfnApplicationVersion(this, name + "-version", versionProps);

        // -- Make sure that Elastic Beanstalk app exists before creating an app version --
        version.addDependsOn(app);

        // IAM role
        var roleProps = RoleProps.builder()
                .assumedBy(new ServicePrincipal("ec2.amazonaws.com"))
                .build();

        var role = new Role(this, name + "-iam-role", roleProps);

        IManagedPolicy policy = ManagedPolicy.fromAwsManagedPolicyName("AWSElasticBeanstalkWebTier");
        role.addManagedPolicy(policy);

        // Cloud Formation profile
        var profileId = name + "-iam-profile";

        var profileProps = CfnInstanceProfileProps.builder()
                .instanceProfileName(profileId)
                .roles(List.of(role.getRoleName()))
                .build();

        var profile = new CfnInstanceProfile(this, profileId, profileProps);

        // Cloud Formation VPC
        var vpcLookupOptions = VpcLookupOptions.builder()
                .isDefault(true)
                .build();

        var vpc = Vpc.fromLookup(this, "default-vpc", vpcLookupOptions);

        String publicSubnets = vpc
                .getPublicSubnets()
                .stream()
                .map(ISubnet::getSubnetId)
                .collect(Collectors.joining(","));

        // HTTP vs HTTPs
        boolean https = props.getCertificateArn() != null;
        boolean http = !https;

        // Cloud Formation environment settings
        List<CfnEnvironment.OptionSettingProperty> settings = new EnvSettingBuilder()

                /* EC2 settings */
                .setting("aws:ec2:instances", "InstanceTypes", "t2.micro")
                .setting("aws:ec2:vpc", "VPCId", vpc.getVpcId())
                .setting("aws:ec2:vpc", "Subnets", publicSubnets)

                /* Elastic Beanstalk settings */
                .setting("aws:elasticbeanstalk:application:environment", "SERVER_PORT", props.getServerPort())
                .setting("aws:elasticbeanstalk:environment", "EnvironmentType", "LoadBalanced")
                .setting("aws:elasticbeanstalk:environment", "LoadBalancerType", "application")
                .setting("aws:elasticbeanstalk:environment:process:default", "HealthCheckPath", props.getHealthCheckPath())

                /* Load balancer settings */
                .settingIf(http, "aws:elbv2:listener:80", "ListenerEnabled", "true")
                .settingIf(http, "aws:elbv2:listener:80", "Protocol", "HTTP")

                .settingIf(https, "aws:elbv2:listener:443", "SSLCertificateArns", props.getCertificateArn())
                .settingIf(https, "aws:elbv2:listener:443", "ListenerEnabled", "true")
                .settingIf(https, "aws:elbv2:listener:443", "Protocol", "HTTPS")

                /* Autoscaling settings */
                .setting("aws:autoscaling:launchconfiguration", "IamInstanceProfile", profile.getRef())
                .setting("aws:autoscaling:asg", "MinSize", "1")
                .setting("aws:autoscaling:asg", "MaxSize", "1")

                .build();

        // Cloud Formation environment
        var envId = name + "-eb-env";

        var envProps = CfnEnvironmentProps.builder()
                .applicationName(app.getApplicationName())
                .environmentName(envId)
                .solutionStackName(props.getStackName())
                .optionSettings(settings)
                .versionLabel(version.getRef())
                .build();

        new CfnEnvironment(this, envId, envProps);
    }

    private static StackProps buildStackProps(ElasticBeanstalkProps props) {
        Environment env = Environment.builder()
                .account(props.getAccount())
                .region(props.getRegion())
                .build();

        return StackProps.builder().env(env).build();
    }

}
