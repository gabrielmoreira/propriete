Propriete is a Java library for abstracting configurations using Java Interface. Heavily inspired by excelent [GWT Constants](http://www.gwtproject.org/doc/latest/DevGuideI18nConstants.html).

## Usage

The Propriete library is available from [Maven Central](http://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22com.github.kevinsawicki%22%20AND%20a%3A%22http-request%22).

```xml
<repositories>
    <repository>
        <id>propriete-mvn-repo</id>
        <url>https://raw.github.com/gabrielmoreira/propriete/mvn-repo/</url>
        <snapshots>
            <enabled>true</enabled>
            <updatePolicy>always</updatePolicy>
        </snapshots>
    </repository>
</repositories>
```

```xml
<dependency>
  <groupId>com.github.gabrielmoreira</groupId>
  <artifactId>propriete</artifactId>
  <version>0.0.2-SNAPSHOT</version>
</dependency>
```

## Examples

### Simple example

```java
public interface Sample {
   @ConfigProperty(name="hello")
   public String greeting();
}
```
```java
Properties properties = new Properties();
properties.put("hello", "World!");

Sample sample = Propriete.getInstance(Sample.class, properties);

assertEquals("World!", sample.value());
```

See more samples [ProprieteTest.java](https://github.com/gabrielmoreira/propriete/blob/master/src/test/java/com/github/gabrielmoreira/propriete/ProprieteTest.java)

## Bugs and Feature requests

You can register bugs and feature requests in the [Github Issue Tracker](https://github.com/gabrielmoreira/propriete/issues).

You're most likely going to paste code and output, so familiarise yourself with
[Github Flavored Markdown](http://github.github.com/github-flavored-markdown/) to make sure it remains readable. Test at [Markable](http://markable.in/).
