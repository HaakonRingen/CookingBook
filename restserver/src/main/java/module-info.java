module restserver {
    
    requires transitive core;
    requires transitive persistence;

    requires spring.web;
    requires spring.beans;
    requires spring.boot;
    requires spring.context;
    requires spring.boot.autoconfigure;

    opens restserver to spring.beans, spring.context, spring.web;

}
