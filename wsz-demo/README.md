wsz

ldp
  cd ~/go/src/github.com/membrane/local-directory-provisioner/ldp/deploy/rbac/
  kubectl create ns ldp
  kubectl apply -f .

metallb
  cd ~/git/p8-infra/kubernetes/metallb
  kubectl create ns metallb-system
  kubectl apply -f lb-config.yaml
  kubectl apply -f lb-def.yaml

istio
  istioctl install --set profile=demo -y
  kubectl edit configmap -n istio-system istio-sidecar-injector
  -> change 'docker.io' to '192.168.137.1'
  
  kubectl edit psp unprivileged
  adding
    allowedCapabilities:
    - NET_ADMIN
    - NET_RAW


postgres-operator
  cd ~/git/p8/postgres-operator
  sed -i 's/registry.opensource.zalan.do/192.168.137.1/' auto/01-configmap.yaml
  kubectl create ns postgres-operator
  kubectl label ns postgres-operator istio-injection=enabled
  kubectl apply -f manual
  kubectl apply -f auto
  
---

kubectl create namespace demo
kubectl label namespace demo istio-injection=enabled

kubectl apply -f 0*
