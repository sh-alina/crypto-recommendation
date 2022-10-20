# crypto-recommendation

Crypto recommendation service.

The service provides a recommendation for crypto investment.

Recommendation service:
● Reads all the prices from the csv files
● Calculates oldest/newest/min/max for each crypto for the whole month
● Exposes an endpoint that will return a descending sorted list of all the cryptos,
comparing the normalized range (i.e. (max-min)/min)
● Exposes an endpoint that will return the oldest/newest/min/max values for a requested
crypto
● Exposes an endpoint that will return the crypto with the highest normalized range for a
specific day

API documentation can be found here: http://localhost:8080/swagger-ui/

====================================
Commands for deploying the application on Kubernetes:

$ kubectl cluster-info
$ kubectl get all
$./mvnw spring-boot:build-image
$ ./mvnw install
$ ./mvnw spring-boot:build-image
$ docker run -p 8080:8080 crypto-recommendation:0.0.1
$ docker cp prices/. bfe8c6a03a53:/workspace/prices
$ docker tag crypto-recommendation:0.0.1 test-task/crypto-recommendation
$ kubectl create deployment demo --image=test-task/crypto-recommendation --dry-run -o=yaml > deployment.yaml
$ echo --- >> deployment.yaml
$ kubectl create service clusterip demo --tcp=8080:8080 --dry-run -o=yaml >> deployment.yaml
$ kubectl apply -f deployment.yaml
$ kubectl get all