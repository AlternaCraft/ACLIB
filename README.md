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
                    <!-- This is just for avoiding problems with plugins which use this library -->
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
Or, if you are going to use the default config file use this:
```JAVA
PluginBase.INSTANCE.init(<Your plugin instance>, <Config loader>);
```
It is explained in Configuration File section

#### Configuration file
You have to create a class which implements ConfigDataInterface for loading the params in your config.
```JAVA
public class ConfigLoader {
    // I did into an inner class for keeping a bit more organized
    public class ConfigDataStore {
        private boolean variable1;

        public void setV1(boolean v1) {
            this.variable1 = v1;
        }

        public boolean isV1() {
            return this.variable1;
        }    
    }

    @Override
    public void loadParams(FileConfiguration fc) { 
        this.setV1(fc.getBoolean("variable1"));
    }
}
```

In order to get it be loaded correctly, you have to pass an instance of "ConfigLoader" to the init method.
```JAVA
ConfigLoader cLoader = new ConfigLoader();
PluginBase.INSTANCE.init(<Your plugin instance>, cLoader);
```
#### Translations
First create your/s message/s class/es of this way:
```JAVA
public enum Messages1 implements LangInterface {
    private final HashMap<Langs, String> locales = new HashMap();
    
    // You can create as many messages as you want
    MESSAGE(
            "&eEsto es un mensaje",
            "&eThis is a message",
            "&eDies ist eine Nachricht",
    );

    /**
     * Define the default languages to load
     *
     * @param es Spanish
     * @param en English
     * @param de German
     */
    private DefineInfo(String es, String en, String de) {
        this.locales.put(Langs.ES, es);
        this.locales.put(Langs.EN, en);
        this.locales.put(Langs.DE, de);
        /* ... */
    }

    @Override
    public String getText(Langs lang) {
        return StringsUtils.translateColors(getDefaultText(lang)); // Get the messages translated
    }

    @Override
    public String getDefaultText(Langs lang) {
        String value = (this.locales.get(lang) == null)
                ? this.locales.get(PluginBase.INSTANCE.getMainLanguage()) : this.locales.get(lang); 

        String v = LangManager.getValueFromFile(lang, this);

        return (v == null) ? value : v;
    }
}
```
And now you have to load the files...
```JAVA
// List of main languages
LangManager.setKeys(Langs.ES, Langs.EN, Langs.DE);
// List of enums to load
LangManager.saveMessages(Messages1.class);
// Load the messages
LangManager.loadMessages();
```

#### Commands
#### Listeners
#### Hookers
#### Exceptions
#### Some utils

### Full example
https://github.com/AlternaCraft/ACLIBExample
