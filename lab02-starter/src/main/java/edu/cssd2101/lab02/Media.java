package edu.cssd2101.lab02;

/** Capability shared by materials consumed over time; physical delivery is a separate concern. */
public interface Media {
    /**
     * Returns playback or estimated reading duration.
     *
     * @return nonnegative whole minutes
     */
    int durationMinutes();

    /**
     * Returns the medium's format.
     *
     * @return nonblank format name
     */
    String format();
}
