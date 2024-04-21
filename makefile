k apply -f postgres-service.yaml,postgres-deployment.yaml,postgres-data-persistentvolumeclaim.yaml
k apply -f orders-service-backend-service.yaml,orders-service-backend-deployment.yaml,orders-service-backend-autosacling.yaml

k get hpa
k get pods

k delete pod,svc --all
k delete --all deployments --namespace=default


aws eks --region us-east-1 update-kubeconfig --name apps_cluster
k port-forward orders-service-backend-svc 30001:30001
