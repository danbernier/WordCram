package wordcram;

public enum WordSkipReason {
	
	/**
     * Skip Reason: the Word was skipped because the WordCram had already drawn
     * the {@link WordCram#maxNumberOfWordsToDraw(int)}.
     * It's really about the Word's rank, its position in the list once the
     * words are sorted by weight: if its rank is greater than the value passed
     * to maxNumberOfWordsToDraw(), then it'll be skipped, and this will be the
     * reason code.
     */
	WAS_OVER_MAX_NUMBER_OF_WORDS("we already reached the maxNumberOfWordsToDraw threshold"),
	
	/**
     * Skip Reason: the Word's shape was smaller than
     * {@link WordCram#minShapeSize(int)}.
     * WordCram will only render words so small, for performance reasons. You
     * can set the minimum Word shape size via
     * {@link WordCram#minShapeSize(int)}.
     */
    SHAPE_WAS_TOO_SMALL("it was below the minShapeSize threshold"),
    
    /**
     * Skip Reason: WordCram tried placing the Word, but it couldn't find a
     * clear spot. The {@link WordNudger} nudged it around a bunch (according
     * to {@link WordCram#maxAttemptsToPlaceWord(int)}, if it was set), but
     * there was just no room.
     */
    NO_SPACE("there wasn't enough space near where you wanted it placed");

    String reason;

    WordSkipReason(String reason) {
        this.reason = reason;
    }

    public String toString() {
        return reason;
    }
}
