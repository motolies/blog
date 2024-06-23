package kr.hvy.blog.module.content.annotation;

public class HtmlSanitizer {
	private static String tagsPattern;
	private static String attrsPattern;
//	private final static String[] tagsTab = { "form", "script", "body", "iframe", "object" };
	private final static String[] tagsTab = { "form", "script", "body", "object" };
	private final static String[] attrsTab = { "FSCommand", "onAbort", "onActivate", "onAfterPrint", "onAfterUpdate", "onBeforeActivate", "onBeforeCopy", "onBeforeCut", "onBeforeDeactivate",
			"onBeforeEditFocus", "onBeforePaste", "onBeforePrint", "onBeforeUnload", "onBeforeUpdate", "onBegin", "onBlur", "onBounce", "onCellChange", "onChange", "onClick", "onContextMenu",
			"onControlSelect", "onCopy", "onCut", "onDataAvailable", "onDataSetChanged", "onDataSetComplete", "onDblClick", "onDeactivate", "onDrag", "onDragEnd", "onDragLeave", "onDragEnter",
			"onDragOver", "onDragDrop", "onDragStart", "onDrop", "onEnd", "onError", "onErrorUpdate", "onFilterChange", "onFinish", "onFocus", "onFocusIn", "onFocusOut", "onHashChange", "onHelp",
			"onInput", "onKeyDown", "onKeyPress", "onKeyUp", "onLayoutComplete", "onLoad", "onLoseCapture", "onMediaComplete", "onMediaError", "onMessage", "onMouseDown", "onMouseEnter",
			"onMouseLeave", "onMouseMove", "onMouseOut", "onMouseOver", "onMouseUp", "onMouseWheel", "onMove", "onMoveEnd", "onMoveStart", "onOffline", "onOnline", "onOutOfSync", "onPaste", "onPause",
			"onPopState", "onProgress", "onPropertyChange", "onReadyStateChange", "onRedo", "onRepeat", "onReset", "onResize", "onResizeEnd", "onResizeStart", "onResume", "onReverse", "onRowsEnter",
			"onRowExit", "onRowDelete", "onRowInserted", "onScroll", "onSeek", "onSelect", "onSelectionChange", "onSelectStart", "onStart", "onStop", "onStorage", "onSyncRestored", "onSubmit",
			"onTimeError", "onTrackChange", "onUndo", "onUnload", "onURLFlip", "seekSegmentTime", "href" };

	static {
		StringBuffer tags = new StringBuffer();
		for (int i = 0; i < tagsTab.length; i++) {
			tags.append(tagsTab[i].toLowerCase());
			if (i < tagsTab.length - 1) {
				tags.append('|');
			}
		}
		tagsPattern = "(?i)</?(" + tags.toString() + "){1}.*?/?>";
	}

	static {
		StringBuffer attrs = new StringBuffer();
		for (int i = 0; i < attrsTab.length; i++) {
			attrs.append(attrsTab[i].toLowerCase());
			if (i < attrsTab.length - 1) {
				attrs.append('|');
			}
		}
		attrsPattern = "(?i)\\s(?:" + attrs.toString() + ")\\s*=\\s*(([\\\"'\\s]?)[^>]*)\\2";
	}

	public static String sanitize(String input) {
		String rtn = input.replaceAll(tagsPattern, "");
		rtn = rtn.replaceAll(attrsPattern, "");
		return rtn;

	}
}
