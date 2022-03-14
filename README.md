##to run integration tests
```cmd
mvn clean install -Pintegration-test
```

##Issues to work
1. the container is built and pushed even when the integration tests fail
2. update this readme to have a complete description of the purpose with objectives for this demonstration repo
3. failing integration-test tests
4. dynamically generate the jar name in the ImageFromDockerfile
5. suppress excessive test container output
