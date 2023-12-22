package dgroomes;

/**
 * A toy class that just holds onto messages in static and instance fields.
 */
public class MessageHolder {

    public static String staticMessage = "Hello";
    public final String instanceMessage;

    public MessageHolder(String instanceMessage) {
        this.instanceMessage = instanceMessage;
    }

    public static String describe() {
        return "MessageHolder{staticMessage='%s', classloader='%s'}".formatted(staticMessage, MessageHolder.class.getClassLoader());
    }

    @Override
    public String toString() {
        return "MessageHolder{instanceMessage='%s'}".formatted(instanceMessage);
    }
}
