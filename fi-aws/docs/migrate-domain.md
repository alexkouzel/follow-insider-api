## Migrating domain to another AWS account

Guide: https://rnanthan.medium.com/migrating-route-53-registered-domains-and-hosted-zones-to-another-aws-account-3645465e0fbe

1. Initiate domain transfer from account A
   ```
   aws route53domains transfer-domain-to-another-aws-account \
      --region us-east-1 \
      --domain-name <domain-name> --account-id <account-id> \
      --profile accountA
   ```
2. Accept domain transfer from account B
   ```
   aws route53domains accept-domain-transfer-from-another-aws-account \
      --region us-east-1 \
      --domain-name <domain-name> \ 
      --password "<password>"
      --profile accountB
   ```
3. Create hosted zone on account B
   ```
   aws route53 create-hosted-zone \
     --name <domain-name> 
     --caller-reference <unique-string>
     --hosted-zone-config Comment="<description>",PrivateZone=false 
     --profile accountB
   ```
4. Get hosted zone ID on account A
   ```
   aws route53 list-hosted-zones-by-name \
   --dns-name "<domain-name>" 
   --profile accountA | grep -m 1 Id 
   ```
5. Create file with hosted zone records
   ```
   aws route53 list-resource-record-sets \
     --hosted-zone-id Z06444843ETO7X4MA2AXA 
     --profile accountA > list-records-Z06444843ETO7X4MA2AXA.txt
   ```
6. Change records according to the guide
7. Create records in a new hosted zone
   ```
   aws route53 change-resource-record-sets \
      --hosted-zone-id Z0940328EZ1TKC7LE8C7
      --change-batch file://~/list-records-Z06444843ETO7X4MA2AXA.txt 
      --profile accountB
   ```
8. Update domain registration
   ```
   aws route53domains update-domain-nameservers \
       --region us-east-1 \
       --domain-name <domain-name> \
       --nameservers Name=ns-30.awsdns-03.com Name=ns-1374.awsdns-43.org Name=ns-1707.awsdns-21.co.uk Name=ns-858.awsdns-43.net
   ```