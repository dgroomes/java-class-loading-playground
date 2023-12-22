package dgroomes;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

import static java.lang.System.out;

/**
 * Please see the README for context.
 */
public class Runner {

    public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException {
        // Can we have two different runtime copies of the "same" class? Let's find out.
        // By "same" I mean the classes have the same binary definition (e.g. the ".class" file). Spoiler alert: we'll
        // find that the same-ness turns into difference.
        Class<?> classFromAppLoader;
        Class<?> classFromCustomLoader;

        // Load the classes
        {
            // This is a familiar syntax. By referencing a class in this way, we are referencing a class object from the
            // same class loader as the current code is running.
            classFromAppLoader = MessageHolder.class;

            // Load a new runtime copy of the MessageHolder class from a custom class loader.
            //
            // In my own learning, I've found this concept particularly hard to grasp because it's so ingrained in my
            // mind that classes are definitional (1 copy in the program) and "instances of a class" are the things that
            // proliferate (many copies). But with class loaders, we can actually create many runtime copies of the same
            // class binary definition (".class" file). This is prototypical "meta" programming.
            //
            // Should we do this? Only in very special circumstances. Be careful of overcomplicating your architecture.
            URL codeSource = MessageHolder.class.getProtectionDomain().getCodeSource().getLocation();
            @SuppressWarnings("resource") var customClassLoader = new URLClassLoader("custom-class-loader", new URL[]{codeSource}, null);
            //noinspection unchecked
            classFromCustomLoader = customClassLoader.loadClass("dgroomes.MessageHolder");
        }

        // Describe the classes
        {
            describe(classFromAppLoader);
            describe(classFromCustomLoader);
            out.println();
        }

        // How do the classes compare to each other? Are they the same?
        {
            // Are the classes logically equal? Answer: no
            out.printf("Logically equal?: %s%n", classFromAppLoader.equals(classFromCustomLoader));
            // Are the classes reference-equal? Answer: no
            out.printf("Reference-equal?: %s%n", classFromAppLoader == classFromCustomLoader);
            out.println();
        }

        // Now that we've established that the classes are different, let's hit the point home by changing the static
        // 'message' field on one of them and observing that the other is unaffected.
        {
            MessageHolder.staticMessage = "Goodbye";
            describe(classFromAppLoader);
            describe(classFromCustomLoader);
            out.println();
        }

        // New them up via reflection
        {
            var instanceFromAppLoader = newInstance(classFromAppLoader, "I'm from the app/system class loader.");
            out.printf("instanceFromAppLoader: %s%n", instanceFromAppLoader);
            var instanceFromCustomLoader = newInstance(classFromCustomLoader, "I'm from a custom class loader.");
            out.printf("instanceFromCustomLoader: %s%n", instanceFromCustomLoader);

            // What are their types? Answer: they both toString as the "dgroomes.MessageHolder" type. But we know that
            // these are actually different runtime classes even though they are named the same.
            out.printf("instanceFromAppLoader type: %s%n", instanceFromAppLoader.getClass());
            out.printf("instanceFromCustomLoader type: %s%n", instanceFromCustomLoader.getClass());

            // Let's do a type check using the familiar "instanceof" operator.
            if (instanceFromAppLoader instanceof MessageHolder)
                out.println("instanceFromAppLoader is an instance of MessageHolder");

            if (!(instanceFromCustomLoader instanceof MessageHolder))
                out.println("instanceFromCustomLoader is NOT an instance of MessageHolder");
        }
    }

    /**
     * This method uses reflection to call the static {@link MessageHolder#describe()} method on the given {@link Class}
     * object. Normally, we would just call the method directly, but we are trying to illustrate how class loaders
     * are able to give us different runtime copies of a same-named class. In a way, class loaders let us have "parallel
     * universes".
     */
    private static void describe(Class<?> clazz) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Method describeMethod = clazz.getMethod("describe");
        var description = (String) describeMethod.invoke(null);
        out.println(description);
    }

    private static Object newInstance(Class<?> clazz, String secondMessage) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        var constructor = clazz.getConstructor(String.class);
        return constructor.newInstance(secondMessage);
    }
}
