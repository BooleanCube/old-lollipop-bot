package mread.controller;

class RApiBuilder {

	public static String buildBrowse(int page) {
		return String.format(RConstants.BROWSE, page);
	}

	public static String buildCatBrowse(int page, String genre) {
		return String.format(RConstants.BROWSE_CAT, RConstants.getGenres().get(genre), page);
	}

	public static String buildeMangaChapter(String mangaUrl) {
		return String.format(RConstants.BASE_URL, mangaUrl);
	}

	public static String buildCombo(String url) {
		return RConstants.BASE_URL.concat(url);
	}
}
