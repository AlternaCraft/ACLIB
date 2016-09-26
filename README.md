# ACLIB
[![Build Status](https://travis-ci.org/AlternaCraft/ACLIB.svg)](https://travis-ci.org/AlternaCraft/ACLIB) [![codecov](https://codecov.io/gh/AlternaCraft/ACLIB/branch/master/graph/badge.svg)](https://codecov.io/gh/AlternaCraft/ACLIB) [ ![Download](https://api.bintray.com/packages/alternacraft/maven/ACLIB/images/download.svg) ](https://www.github.com/alternacraft/ACLIB/releases) [![Web](https://img.shields.io/badge/Web-alternacraft.github.io%2FACLIB%2F-yellow.svg)](https://alternacraft.github.io/ACLIB)

This is a custom library used in plugins created by AlternaCraft.

### How to integrate
```XML
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-shade-plugin</artifactId>
    <version>2.4.3</version>
    <executions>
        <execution>
            <id>libs</id>
            <phase>package</phase>
            <goals>
                <goal>shade</goal>
            </goals>
            <configuration>
                <relocations>
                    <relocation>
                        <pattern>com.alternacraft.aclib</pattern>
                        <shadedPattern>${project.groupId}.${project.artifactId}.ACLIB</shadedPattern>
                    </relocation>
                </relocations>
            </configuration>
        </execution>      
    </executions>
</plugin>
```

### How to use
#### First at all, register your plugin.
```JAVA
PluginBase.INSTANCE.init(<Your plugin instance>);
```
### Configuration file
### Translations
### Commands
### Listeners
### Hookers
### Exceptions
### Some utils
