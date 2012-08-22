Build Result Notifier Maven Plugin
===================================

This plugin makes to notify build result by maven.

![Notification](https://raw.github.com/kompiro/notification-maven-plugin/master/src/site/resources/images/notification.png)

How to use it?
---------------

Add plugin repository to pom.xml

    <pluginRepositories>
      <pluginRepository>
        <id>kompiro.org</id>
        <url>http://kompiro.org/maven/</url>
        <releases>
          <enabled>true</enabled>
        </releases>
        <snapshots>
          <enabled>false</enabled>
        </snapshots>
      </pluginRepository>
    </pluginRepositories>

Add the plugin to pom.xml's <build>

    <build>
      ...
      <plugins>
        ...
        <plugin>
          <groupId>org.kompiro.notification</groupId>
          <artifactId>notification-maven-plugin</artifactId>
          <version>0.8.3</version>
        </plugin>
        ...
      </plugins>
      ...
    </build>

And run `mvn notification:notify`

Tips and Tricks
------------------

Add notification goal automatically -> add execution

      <plugins>
        ...
        <plugin>
          <groupId>org.kompiro.notification</groupId>
          <artifactId>notification-maven-plugin</artifactId>
          <version>0.8.3</version>
          <executions>
            <execution>
              <id>notification</id>
              <phase>initialize</phase>
              <goals>
                <goal>notify</goal>
              </goals>
            </execution>
          </executions>
        </plugin>
        ...
      </plugins>

If you'd like to use growl, set <configuration/>

      <plugins>
        ...
        <plugin>
          <groupId>org.kompiro.notification</groupId>
          <artifactId>notification-maven-plugin</artifactId>
          <version>0.8.3</version>
          <configuration>
            <strategy>growl</strategy>
          </configuration>
          <executions>
            <execution>
              <id>notification</id>
              <phase>initialize</phase>
              <goals>
                <goal>notify</goal>
              </goals>
            </execution>
          </executions>
        </plugin>
        ...
      </plugins>
