package pl.edu.utp.mybookshelf.model;

public enum BookState {

    TO_READ("Do przeczytania"),
    READ("Przeczytane");

    public final String title;

    BookState(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return this.title;
    }

}
