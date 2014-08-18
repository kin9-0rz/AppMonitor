package hook.xposed;

abstract class XHook{
	abstract String getClassName();
	abstract void hook(String pkgName, ClassLoader classLoader);
}