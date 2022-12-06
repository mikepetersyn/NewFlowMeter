# NewFlowMeter

## Copyright
This is a fork of the [CICFlowmeter-V4.0](https://github.com/ahlashkari/CICFlowMeter), refactoring the application into a pure CMDApp and adding a Kafka sink. See the [license information](LICENSE.txt) and original [Readme](CICReadme.txt).

## Usage

Start Class Cmd() for starting the application.

For reading in PCAP files and producing into kafka topics use the application with the following parameters:

`-p path/to/pcap -k localhost:9092 topic-name group-id`

For listening on a network interfaces (execute as root) and producing into kafka topic use the following parameters:

`-i interface-name -k localhost:9092 topic-name group-id`

## Configuration

For the usage of the jnetpcap artifact the maven package must be installed locally with the following command:
    //linux :at the pathtoproject/jnetpcap/linux/jnetpcap-1.4.r1425
    //windows: at the pathtoproject/jnetpcap/win/jnetpcap-1.4.r1425
    mvn install:install-file -Dfile=jnetpcap.jar -DgroupId=org.jnetpcap -DartifactId=jnetpcap -Dversion=1.4.1 -Dpackaging=jar

Alternative adding the right repository configuration and right artifact dependency configuration in the pom.xml:
```
    <!-- https://mvnrepository.com/artifact/jnetpcap/jnetpcap -->
    <dependency>
        <groupId>jnetpcap</groupId>
        <artifactId>jnetpcap</artifactId>
        <version>1.4.r1425-1g</version>
    </dependency>
```


## Known Issues

Concerning java.lang.UnsatisfiedLinkError: com.slytecths.libarary.NativeLibrary.dlopen(Ljava/lang/String;)
consider the following link: https://stackoverflow.com/questions/39048964/jnetpcap-java-lang-unsatisfiedlinkerror-com-slytechs-library-nativelibrary-dl stating the following in summary:

1) install libpcap-devel
2) download jnetpcap-1.4.r1425
3) extract libjnetpcap.so to /lib64

-------

