Write-Host "Deploying KubeSentry..."

kubectl apply -f .\k8s\namespace.yaml
kubectl apply -f .\k8s\config\
kubectl apply -f .\k8s\postgres\
kubectl apply -f .\k8s\app\

Write-Host "Deployment completed."