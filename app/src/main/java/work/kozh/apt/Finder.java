package work.kozh.apt;


import work.kozh.apt.provider.Provider;

public interface Finder<T> {
    void inject(T host, Object source, Provider provider);
}
