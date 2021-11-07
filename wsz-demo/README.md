wsz
  setupVm
  prepareOfflineCluster
  reconcileAddons

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
  istioctl install --set profile=demo -y --set values.global.hub="192.168.137.1/istio" --set meshConfig.enableTracing=true

##  --set values.global.tracer.zipkin.address=jaeger-collector.istio-system:9411 --set meshConfig.defaultConfig.tracing.sampling=100
  
  kubectl edit psp unprivileged
  adding
    allowedCapabilities:
    - NET_ADMIN
    - NET_RAW
	
	
	kubectl apply -f https://raw.githubusercontent.com/istio/istio/release-1.11/samples/addons/grafana.yaml
	kubectl apply -f https://raw.githubusercontent.com/istio/istio/release-1.11/samples/addons/prometheus.yaml
	kubectl apply -f https://raw.githubusercontent.com/istio/istio/release-1.11/samples/addons/jaeger.yaml
	kubectl apply -f https://raw.githubusercontent.com/istio/istio/release-1.11/samples/addons/kiali.yaml

  insert in 'kiali' configmap:
    external_services
      grafana:
        in_cluster_url: "http://grafana.istio-system:3000"



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
-> runs

kubectl apply -f 1*
-> accessible via http://istio-ingress.wsz.predic8.de/v1/bill?month=2019-11&receiverUsername=max

kubectl apply -f 2*

