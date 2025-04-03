module core {

    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.annotation;

    opens core to com.fasterxml.jackson.databind;
    
    exports core;
}
