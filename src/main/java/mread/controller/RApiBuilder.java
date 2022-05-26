package mread.controller;

/**
 * API Builder for Readm Library
 */
class RApiBuilder {

    /**
     * Get browse link
     * @param page page number
     * @return string
     */
	public static String buildBrowse(int page) {
		return String.format(RConstants.BROWSE, page);
	}

    /**
     * Get cat browse link
     * @param page page number
     * @param genre genre name
     * @return string
     */
	public static String buildCatBrowse(int page, String genre) {
		return String.format(RConstants.BROWSE_CAT, RConstants.getGenres().get(genre), page);
	}

    /**
     * Get manga chapter
     * @param mangaUrl manga url
     * @return string
     */
	public static String buildeMangaChapter(String mangaUrl) {
		return String.format(RConstants.BASE_URL, mangaUrl);
	}

    /**
     * Get manga combo
     * @param url url
     * @return string
     */
	public static String buildCombo(String url) {
		return RConstants.BASE_URL.concat(url);
	}

}
