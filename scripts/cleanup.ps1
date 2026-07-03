Write-Host "Cleaning resources..."

kubectl delete -f .\k8s\app\
kubectl delete -f .\k8s\postgres\
kubectl delete -f .\k8s\config\
kubectl delete -f .\k8s\namespace.yaml

Write-Host "Cleanup completed."