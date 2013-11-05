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

        // add a shutdown hook to write out the in memory cookies
        Runtime.runtime.addShutdownHook(new Thread(this))
    }

    public void initCookieHandler(CookieHandler cookieHandler) {
        String str = preferences.get("colt.cookies", "")
        println "out str = $str"
        if (str == "") {
            return
        }
        str.split("]::").each {
            String[] arr = it.split(/\[/)
            URI uri = URI.create(arr[0])
            Map<String, List<String>> headers = new LinkedHashMap<String, List<String>>()
            List<String> list = new ArrayList<>()
            arr[1].split(", ").each {s ->
                list.add(s)
            }
            headers.put("Set-Cookie2", list)
            cookieHandler.put(uri, headers)
        }

    }

    private void save() {
        String str = ""
        getURIs().each {
            str += it.toString() + get(it).toString() + "::"
        }
        preferences.put("colt.cookies", str)
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
