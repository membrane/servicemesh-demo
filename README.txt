Diese Git Repository begleitet das Youtube Video "Servicemesh in der Praxis: Istio".

# Demo Plattform

Nicht im Video beschrieben wurde das Setup der Demo Plattform:

- Kubernetes 1.16 Cluster
  eigene Offline-Installation auf 4 virtuellen Maschinen
- Local Directory Provisioner https://github.com/membrane/local-directory-provisioner
  stellt Storage-Unterstützung durch lokale Verzeichnisse auf den Kubernetes Knoten zur Verfügung
  (Das erzeugt im Zusammenspiel mit dem verwendeten postgres Docker Image einen
  Single-Point-of-Failure (SPOF), ist aber für Demozwecke kein Problem.)
- MetalLB https://github.com/danderson/metallb
  stellt Unterstützung für Kubernetes Services vom Typ LoadBalancer zur Verfügung.
- Istio 1.4.2
  - Installation basiert auf https://istio.io/docs/setup/install/helm/
  - allerdings wurde die Zeile
        in_cluster_url: {{ .Values.dashboard.jaegerURL }}
    in istio\charts\kiali\templates\configmap.yaml eingefügt
  - Die helm Kommandozeilenoptionen
    ```
	 --namespace istio-system --values istio/values-istio-demo.yaml --set global.proxy.image=192.168.137.1/istio/proxyv2:1.4.2 --set global.proxy_init.image=192.168.137.1/istio/proxyv2:1.4.2 --set "kiali.dashboard.jaegerURL=http://jaeger-query:16686" --set "kiali.dashboard.jaegerURL=http://jaeger-query:16686" --set "kiali.dashboard.grafanaURL=http://grafana:3000" --set "gateways.istio-ingressgateway.externalTrafficPolicy=Local"
	```
	wurden verwendet. Sie
	- verwenden prinzipiell eine von Istio mitgelieferte Beispielkonfiguration,
	- korrigieren allerdings ein paar Istio-interne Verdrahtungen,
	- stellen - im Zusammenspiel mit MetalLB - die echte Client IP Adresse dem ingressgateway von Istio zur Verfügung,
	- erlauben es, die Istio sowie die Demo offline zu installieren bzw. zu betreiben.
      - Dabei wird ein lokaler Docker Hub Proxy (192.168.137.1) verwendet.
  - Es wurde NET_ADMIN unter allowedCapabilities in eine Kubernetes PodSecurityPolicy hinzugefügt, zu deren Verwendung der aktuell am Cluster angemeldete Benutzer berechtigt ist.
