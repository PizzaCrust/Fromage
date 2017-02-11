package online.pizzacrust.fromage.common;

import org.luaj.vm2.Globals;
import org.luaj.vm2.LoadState;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.compiler.LuaC;
import org.luaj.vm2.lib.Bit32Lib;
import org.luaj.vm2.lib.CoroutineLib;
import org.luaj.vm2.lib.PackageLib;
import org.luaj.vm2.lib.StringLib;
import org.luaj.vm2.lib.TableLib;
import org.luaj.vm2.lib.jse.JseBaseLib;
import org.luaj.vm2.lib.jse.JseIoLib;
import org.luaj.vm2.lib.jse.JseMathLib;
import org.luaj.vm2.lib.jse.JseOsLib;

public class LuaObjectTest extends LuaObject {

    public LuaObjectTest() {
        this.processClass();
    }

    @LuaFunction
    public void objectTest(Varargs varargs) {
        System.out.println("meow");
    }

    public static class TestProvider extends LuaObject.Static {

        public TestProvider() {
            this.processClass();
        }

        @LuaFunction
        public LuaValue getLuaObjectTest(Varargs varargs) {
            System.out.println("Static works!");
            return new LuaObjectTest();
        }
    }

    public static void main(String... args) throws Exception {
        Globals globals = new Globals();
        globals.load(new JseBaseLib());
        globals.load(new PackageLib());
        globals.load(new Bit32Lib());
        globals.load(new TableLib());
        globals.load(new StringLib());
        globals.load(new CoroutineLib());
        globals.load(new JseMathLib());
        globals.load(new JseIoLib());
        globals.load(new JseOsLib());
        globals.load(new TestProvider());
        LoadState.install(globals);
        LuaC.install(globals);
        LuaValue chunk = globals.load("TestProvider.getLuaObjectTest().objectTest()");
        chunk.call();
    }

}
