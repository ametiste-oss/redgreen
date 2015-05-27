# Indtroduction
Redgreen is simple resource-failover service designed to provide resilent and robust entrypoints.

Based on [Netflix Hystrix](https://github.com/Netflix/Hystrix) library, the service may be used to 
isolate points of access to entire system, maintaine system availability during emergent shutdowns 
(deploys, crashes, unexpected restarts), and so forth. 

Redgreen is standalone application, so it could be used and integrated within a system at the operational phase, 
on the fly, no internal changes in a modules of system are required for integration and usage.

# At a Glance

Redgreen application is based on the concept of _green-red resources bundle_. 

Main idea of the _bundles_ - a bundle has the one _green_ resource - primary resource that used to 
handle incoming requests, and several _red_ resources - backup resources that will help, 
if the primary one was failed. 

Redgreen application allow to define a set of _resource bundles_, and provide interfaces to 
handle incoming requests using defined _resource bundles_.

# Installation

_Note, redgreen application is requred installed JDK 8 for build and JRE 8 for run._

To install Redgreen application from pre-built distribution just download distribution bundle:

    wget https://github.com/ametiste-oss/redgreen/releases/download/v0.1.0/ametiste-redgreen-0.1.0-RELEASE.zip
    unzip ametiste-redgreen-0.1.0-RELEASE.zip
    cd ametiste-redgreen-0.1.0-RELEASE

To install Redgreen application from the source just clone/download repository/sources and 
run gradle command to build spring-boot application, for example:

    wget https://github.com/ametiste-oss/redgreen/archive/v0.1.0.zip
    unzip v0.1.0.zip
    cd redgreen-0.1.0/
    ./gradlew installApp
    cd ./build/install
  
After the build was done, the ./build/install directory would contain spring-boot application distribution.

# Usage Example

To configure _resource bundles_ just create file _application.properties_ in the distribution directory:

    touch ./application.properties

And add several bundles in the format:

    redgreen.direct.bundles.[BUNDLE_NAME].[red|green]=[RESOURCE_URI]
  
Where BUNDLE_NAME - name of the bundle, this name will be used to compose bundle URL later, and
RESOURCE_URI - the URI of bundeled resource.
  
For example ( _note, the green resource URL is intentionally broken to demo failover capabilities_ ):

    echo "redgreen.direct.bundles.my-test-bundle.green=http://example.xcom/test-redgreen-pls" >> application.properties
    echo "redgreen.direct.bundles.my-test-bundle.red=http://example.com/" >> application.properties

_Note, the one *green* and at least one *red* resource is required for each bundle. List of *red* resources
could be defined using comma seprated list of strings: http://foo/,http://bar/._

After this you could run redgreen applicaton using run-script from the ./bin directory:

    ./bin/ametiste-redgreen --spring.config.location=`pwd`/application.properties

_Note, by default redgreen application will start on the port 8080, you can change it using spring-boot property
--server.port, for example --server.port=9090_
  
Now you can try it, just query the http://127.0.0.1:8080/my-test-bundle URL by a browser or any http client. 
In the application log ( probably stdout ) you should see the line, that indicates that 
the _green_ resource was failed:

    ERROR 76471 --- [ HystrixTimer-1] o.a.r.a.HystrixSimpleFailoverLine : Green resource failed, performing fallback for: my-test-bundle

but, the _red_ one is done failover process and you should get the content of _example.com_ resource.

_Note, only GET, OPTIONS and HEAD request methods are supported, the methods that can change a state of server is 
out the scope of redgreen application._
