# Flexion's java challenge implementation for job interview 

## About the implementation

This interview challenge is implemented with Maven and JUnit 5.  
The provided jar files are stored in the git repository in the scope of the maven project directory and included in the POM as a dependency.  
The integration test gets executed as a JUnit test case with other extra tests as well.  
The required `devId` variable can be specified externally both in the environment and as a JVM argument, which makes it easier to both use for developers and integration into a CI tool.

### Run tests

Go to the directory `./source/JavaChallenge/` and execute `mvn test -Dflexion.javachallenge.dev_id=...` (specify you `dev_id`).  
You can inspect the generated purchases at `http://sandbox.flexionmobile.com/javachallenge/rest/developer/{dev_id}/all`.

### Run in docker
If you don't have the right software environment, there is a docker build file in the repository that builds one where it is more likely to work.

In this case, you have to specify the `devId` as an environment variable to pass through into the docker container.  
To run tests in docker, execute this: `FLEXION_JAVACHALLENGE_DEV_ID=... ./scripts/docker_run_tests.sh`.


## Possible improvements

### Use maven instead of jars.
This kind of library distribution (I mean sending/downloading jar files)
is deprecated. The main reason: it scales bad and is difficult to use in an integration system
1) Binary files are not friends with git repos: 
  As the project evolves and libraries get updated, old jar files
  become unwanted elements. But because git keeps all historical files,
  it's taking space as old commit files.
2) If JARs are not stored in the repository, it causes difficulties to manage
  the project in CI software.
3) When tests become more complex and start picking up dependencies.
  Well, managing dependencies by downloading a set of jar files is a way
  to hell. 

What to do instead:
	Create a maven repository and publish dependencies there.
It doesn't require though infrastructure, see my maven repository:
https://maven.javaexperience.eu/

Benefits:
1) Easy to manage previous versions of tests without polluting git.
2) Common library utility classes, source codes, heavy resource files, and
   even tests cases can be shared using a maven repository.
3) Easy to integrate. Just add a \<repository\> entry in POM, reference the
   utility you want to use, and reference a test set version you want to satisfy.
   This works well with Maven CLI and with Jenkins as well.


### Tests

I implemented 4 extra tests (6 extra cases) related to the usage of the implemented connector, including fancy user inputs, null input, and double consumption.  
See `hu.dankodavid.flexion.javachallenge.TestPurchase` for the test cases.

