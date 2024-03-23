# cs5324-spring-docker

A Repo built for creating a Spring/Docker tutorial

## Create deployable file

- Maven:
  - `$ mvn install`
  - should create a runnable JAR file at `./target/` directory

## Docker setup

*Requires docker be running on local machine for following steps*

1. **Create `Dockerfile` in root of project**

```dockerfile
FROM amazoncorretto:17-alpine-jdk
VOLUME /tmp
COPY target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
```

- Line 1
  - This is the JDK base image - needs to match JDK or JRE used to devolp application
- Line 2
  - creting a temporay mount for the container to use while running
- Line 3
  - Copying the JAR file created in the previous step into the image and renaming it to `app.jar`
- Line 4
  - runs the following command when the container starts: `$ java -jar /app.jar`
  - this enables starts the application that has been copied over

2. **Build the image**

- `$ docker build -t <group-name>/<project-name>:<version>`
  - `<group-name>`
    - Replace this with the username or group name of who will be uploading to dockerhub.  If not uploading to docker hub this can be anything desired
  - `<project-name>`
    - replace with the name of the repository on docker hub, or as the project name of what is being worked on
  - `<version>`
    - replace with version of application, for testing `latest` is acceptable

3. **Test run newly created image**

- `$ docker run -p 8080:8080 --name <name> <group-name>/<project-name>:<version>`
  - `-p 8080:8080`
    - enables port `8080` from inside the container to be exposed on the local machine
  - `--name <name>`
    - this is an optional argument to name the container once it is running
  - `<group-name>/<project-name>:<version>`
    - this should match what was created with the `docker build` command above
- After running this command, the Spring logging will be shown in the terminal.  This can be used to verify the application is running.  For testing, a simple `/isAvailable` API is available to call to test that the port forwarding is working
- Be sure to stop the container once done testing to not interfere with later steps

4. **Pushing to dockerhub**

- `$ docker push <group-name>/<project-name>:<version>`
  - This command will push to error if a the `<group-name>/<project-name>` does not exist
  - once pushed, can be seen on dockerhub by going to `hub.docker.com/r/<group-name>/<project-name>`

## Kubernetes and Minikube

*written from perspective of Windows 10 setup - refer to [Setting up minikube](#helpful-links) in the helpful links for further documentation*

1. **Install Minikube**

- Open a administrator privlidged Powershell window
- `$ winget install minikube`
- once installed, run `minikube start` to start the k8s cluster
- can verify it is running with `kubectl get all` to see the base services running

2. **Create deployment file**

- In root directory, create the following file to start:
  - `/k8s/deploy.yaml`
- inside `deploy.yaml` create the following yaml file:

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: <deployment name>
  labels:
    app: <application name>
spec:
  replicas: <number of replicas desired>
  selector:
    matchLabels:
      app: <application name>
  template:
    metadata:
      labels:
        app: <application name>
    spec:
      containers:
        - name: <container name>
          image: <group-name>/<project-name>:<version>
```

- This is a basic deployment script, it will create a simple deployment with the specified names (ensure to match where matching above) with a desired number of replicas
- In this instance, the image must be available locally to deploy.  utilize `docker pull` prior to deployment if the image is not already available

3. **Deploy k8s cluster**

- `kubectl apply -f ./k8s/deploy.yaml`
- This will start the deployment
- check progress with `kubectl get all` to see all running services
  - with this command we should see the number of pods is equal to the number of replicas provided in the `deploy.yaml` file

4. **Expose port and test!**

- Even though this is now deployed, there are no exposed ports for kubernetes yet
- run `kubectl port-forward pod/<pod-id> 8080:8080`
  - `<pod-id>` should be one of the pods found when running the `kubectl get all` command
- Now, either using `curl` or Postman, attempt to call one of the endpoints in your application
  - you can monitor the logs by running `kubectl logs -f pod/<pod-id>`
  - this will stream the logs, so if logging shows when a call is made you will see it in real time!

## Helpful Links

- Spring.io tutorial/guide
  - [spring-boot-docker](https://spring.io/guides/topicals/spring-boot-docker)
- Guide written by Docker
  - [docker and docker-compose](https://www.docker.com/blog/kickstart-your-spring-boot-application-development/)
- Youtube demo #1
  - [Video](https://www.youtube.com/watch?v=RVIbMuNs1aw&ab_channel=JavaGuides)
  - [Blog](https://www.javaguides.net/2021/09/dockerizing-spring-boot-application.html)
- Medium Blo post #1
  - [Medium Docker with Compose](https://medium.com/trantor-inc/developing-spring-boot-applications-in-docker-locally-4ec922f4cb45)
- 9 tips from Docker
  - [Docker blog #1](https://www.docker.com/blog/9-tips-for-containerizing-your-spring-boot-code/)
- Setting up Minikube and k8s
  - [Minikube docs](https://minikube.sigs.k8s.io/docs/start/)
