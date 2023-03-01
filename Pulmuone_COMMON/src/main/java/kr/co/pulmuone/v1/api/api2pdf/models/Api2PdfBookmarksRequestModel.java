/*
MIT License

Copyright (c) 2018 Api2Pdf

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/

package kr.co.pulmuone.v1.api.api2pdf.models;

import kr.co.pulmuone.v1.api.api2pdf.models.Api2PdfBookmarkItemModel;

public class Api2PdfBookmarksRequestModel extends Api2PdfRequestModelBase {
	private String url;
	private Api2PdfBookmarkItemModel[] bookmarks;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Api2PdfBookmarkItemModel[] getBookmarks() {
		return bookmarks;
	}

	public void setBookmarks(Api2PdfBookmarkItemModel[] bookmarks) {
		this.bookmarks = bookmarks;
	}
}