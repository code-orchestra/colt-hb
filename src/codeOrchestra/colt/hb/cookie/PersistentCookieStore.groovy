package codeOrchestra.colt.hb.cookie;


import java.util.prefs.Preferences

/**
 * @author Dima Kruk
 */
public class PersistentCookieStore implements CookieStore, Runnable {
    CookieStore store
    Preferences preferences

    public PersistentCookieStore() {
        // get the default in memory cookie store
        store = new CookieManager().cookieStore

        preferences = Preferences.userNodeForPackage(PersistentCookieStore)

        //read in cookies from persistant storage
        String str = preferences.get("colt.hb.cookies", "")
        println "out str = $str"
        str.split("]::").each {
            if(it != "") {
                String[] arr = it.split(/\[/)
                URI uri = new URI(arr[0])
                HttpCookie.parse(arr[1]).each {
                    add(uri, it)
                }
            }
        }
        // and add them store

        // add a shutdown hook to write out the in memory cookies
        Runtime.runtime.addShutdownHook(new Thread(this))
    }

    private void save() {
        String str = ""
        getURIs().each {
            str += it.toString() + get(it).toString() + "::"
        }
        preferences.put("colt.hb.cookies", str)
        preferences.sync()

        getURIs().each {
            println "$it = ${get(it)}"
        }
    }

    public void run() {
        //write cookies in store to persistent storage
        save()
    }

    public void	add(URI uri, HttpCookie cookie) {
        store.add(uri, cookie)
    }

    public List<HttpCookie> get(URI uri) {
        return store.get(uri)
    }

    public List<HttpCookie> getCookies() {
        return store.cookies
    }

    public List<URI> getURIs() {
        return store.URIs
    }

    public boolean remove(URI uri, HttpCookie cookie) {
        return store.remove(uri, cookie)
    }

    public boolean removeAll()  {
        return store.removeAll()
    }
}
