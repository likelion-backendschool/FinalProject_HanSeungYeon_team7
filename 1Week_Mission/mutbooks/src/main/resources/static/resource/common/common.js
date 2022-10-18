toastr.options = {
    closeButton: false,
    debug: false,
    newestOnTop: false,
    progressBar: false,
    positionClass: "toast-top-right",
    preventDuplicates: false,
    onclick: null,
    showDuration: "300",
    hideDuration: "1000",
    timeOut: "5000",
    extendedTimeOut: "1000",
    showEasing: "swing",
    hideEasing: "linear",
    showMethod: "fadeIn",
    hideMethod: "fadeOut"
};

function successModal(msg) {
    toastr["success"](msg);
}

function errorModal(msg) {
    toastr["error"](msg);
}

function warningModal(msg) {
    toastr["warning"](msg);
}


console.clear();

// 토스트 에디터 시작

// 토스트 에디터 - 라이브러리 - 시작
function ToastEditor__getUriParams(uri) {
    uri = uri.trim();
    uri = uri.replaceAll("&amp;", "&");
    if (uri.indexOf("#") !== -1) {
        let pos = uri.indexOf("#");
        uri = uri.substr(0, pos);
    }

    let params = {};

    uri.replace(/[?&]+([^=&]+)=([^&]*)/gi, function (str, key, value) {
        params[key] = value;
    });
    return params;
}

function ToastEditor__escape(origin) {
    return origin
        .replaceAll("<t-script", "<script")
        .replaceAll("</t-script", "</script");
}

function ToastEditor__getAttrValue($el, attrName, defaultValue) {
    const value = $el.attr(attrName);

    if (!value) {
        return defaultValue;
    }

    return value;
}

// 토스트 에디터 - 라이브러리 - 끝

// 토스트 에디터 - 플러그인 - 시작
const ToastEditor__chartOptions = {
    minWidth: 100,
    maxWidth: 600,
    minHeight: 100,
    maxHeight: 300
};

function ToastEditor__PluginYoutube() {
    const toHTMLRenderers = {
        youtube(node) {
            const html = renderYoutube(node.literal);

            return [
                { type: "openTag", tagName: "div", outerNewLine: true },
                { type: "html", content: html },
                { type: "closeTag", tagName: "div", outerNewLine: true }
            ];
        }
    };

    function renderYoutube(uri) {
        uri = uri.replace("https://www.youtube.com/watch?v=", "");
        uri = uri.replace("http://www.youtube.com/watch?v=", "");
        uri = uri.replace("www.youtube.com/watch?v=", "");
        uri = uri.replace("youtube.com/watch?v=", "");
        uri = uri.replace("https://youtu.be/", "");
        uri = uri.replace("http://youtu.be/", "");
        uri = uri.replace("youtu.be/", "");

        let uriParams = ToastEditor__getUriParams(uri);

        let width = "100%";
        let height = "100%";

        let maxWidth = 500;

        if (!uriParams["max-width"] && uriParams["ratio"] == "9/16") {
            uriParams["max-width"] = 300;
        }

        if (uriParams["max-width"]) {
            maxWidth = uriParams["max-width"];
        }

        let ratio = "16/9";

        if (uriParams["ratio"]) {
            ratio = uriParams["ratio"];
        }

        let marginLeft = "auto";

        if (uriParams["margin-left"]) {
            marginLeft = uriParams["margin-left"];
        }

        let marginRight = "auto";

        if (uriParams["margin-right"]) {
            marginRight = uriParams["margin-right"];
        }

        let youtubeId = uri;

        if (youtubeId.indexOf("?") !== -1) {
            let pos = uri.indexOf("?");
            youtubeId = youtubeId.substr(0, pos);
        }

        return (
            '<div style="max-width:' +
            maxWidth +
            "px; margin-left:" +
            marginLeft +
            "; margin-right:" +
            marginRight +
            "; aspect-ratio:" +
            ratio +
            ';" class="relative"><iframe class="absolute top-0 left-0 w-full" width="' +
            width +
            '" height="' +
            height +
            '" src="https://www.youtube.com/embed/' +
            youtubeId +
            '" frameborder="0" allow="accelerometer; autoplay; encrypted-media; gyroscope; picture-in-picture" allowfullscreen></iframe></div>'
        );
    }
    // 유튜브 플러그인 끝

    return { toHTMLRenderers };
}

// katex 플러그인
function ToastEditor__PluginKatex() {
    const toHTMLRenderers = {
        katex(node) {
            let html = katex.renderToString(node.literal, {
                throwOnError: false
            });

            return [
                { type: "openTag", tagName: "div", outerNewLine: true },
                { type: "html", content: html },
                { type: "closeTag", tagName: "div", outerNewLine: true }
            ];
        }
    };

    return { toHTMLRenderers };
}

function ToastEditor__PluginCodepen() {
    const toHTMLRenderers = {
        codepen(node) {
            const html = renderCodepen(node.literal);

            return [
                { type: "openTag", tagName: "div", outerNewLine: true },
                { type: "html", content: html },
                { type: "closeTag", tagName: "div", outerNewLine: true }
            ];
        }
    };

    function renderCodepen(uri) {
        let uriParams = ToastEditor__getUriParams(uri);

        let height = 400;

        let preview = "";

        if (uriParams.height) {
            height = uriParams.height;
        }

        let width = "100%";

        if (uriParams.width) {
            width = uriParams.width;
        }

        if (!isNaN(width)) {
            width += "px";
        }

        let iframeUri = uri;

        if (iframeUri.indexOf("#") !== -1) {
            let pos = iframeUri.indexOf("#");
            iframeUri = iframeUri.substr(0, pos);
        }

        return (
            '<iframe height="' +
            height +
            '" style="width: ' +
            width +
            ';" scrolling="no" title="" src="' +
            iframeUri +
            '" frameborder="no" allowtransparency="true" allowfullscreen="true"></iframe>'
        );
    }

    return { toHTMLRenderers };
}
// 유튜브 플러그인 끝

// repl 플러그인 시작
function ToastEditor__PluginRepl() {
    const toHTMLRenderers = {
        repl(node) {
            const html = renderRepl(node.literal);

            return [
                { type: "openTag", tagName: "div", outerNewLine: true },
                { type: "html", content: html },
                { type: "closeTag", tagName: "div", outerNewLine: true }
            ];
        }
    };

    function renderRepl(uri) {
        var uriParams = ToastEditor__getUriParams(uri);

        let uriBits = uri.split("#");
        const hash = uriBits.length == 2 ? uriBits[1] : "";
        uriBits = uriBits[0].split("?");

        const newUrl = uriBits[0] + "?embed=true#" + hash;

        var height = 400;

        if (uriParams.height) {
            height = uriParams.height;
        }

        return (
            '<iframe frameborder="0" width="100%" height="' +
            height +
            'px" src="' +
            newUrl +
            '"></iframe>'
        );
    }

    return { toHTMLRenderers };
}
// 토스트 에디터 - 플러그인 - 끝

// 토스트 에디터 - 에디터 초기화 - 시작
function ToastEditor__init() {
    $(".toast-ui-editor, .toast-ui-viewer").each(function (index, node) {
        const $node = $(node);
        const isViewer = $node.hasClass("toast-ui-viewer");
        const $initialValueEl = $node.find(" > script");
        const initialValue =
            $initialValueEl.length == 0
                ? ""
                : ToastEditor__escape($initialValueEl.html().trim());

        const placeholder = ToastEditor__getAttrValue(
            $node,
            "toast-ui-editor--placeholder",
            ""
        );
        const previewStyle = ToastEditor__getAttrValue(
            $node,
            "toast-ui-editor--previewStyle",
            "vertical"
        );
        const height = ToastEditor__getAttrValue(
            $node,
            "toast-ui-editor--height",
            "600px"
        );
        const theme = ToastEditor__getAttrValue(
            $node,
            "toast-ui-editor--theme",
            "light"
        );

        const editorConfig = {
            el: node,
            viewer: isViewer,
            previewStyle: previewStyle,
            initialValue: initialValue,
            placeholder: placeholder,
            height: height,
            theme: theme,
            plugins: [
                [toastui.Editor.plugin.chart, ToastEditor__chartOptions],
                [toastui.Editor.plugin.codeSyntaxHighlight, { highlighter: Prism }],
                toastui.Editor.plugin.tableMergedCell,
                toastui.Editor.plugin.colorSyntax,
                [
                    toastui.Editor.plugin.uml,
                    { rendererURL: "http://www.plantuml.com/plantuml/svg/" }
                ],
                ToastEditor__PluginKatex,
                ToastEditor__PluginYoutube,
                ToastEditor__PluginCodepen,
                ToastEditor__PluginRepl
            ],
            customHTMLSanitizer: (html) => {
                return (
                    DOMPurify.sanitize(html, {
                        ADD_TAGS: ["iframe"],
                        ADD_ATTR: [
                            "width",
                            "height",
                            "allow",
                            "allowfullscreen",
                            "frameborder",
                            "scrolling",
                            "style",
                            "title",
                            "loading",
                            "allowtransparency"
                        ]
                    }) || ""
                );
            }
        };

        const editor = isViewer
            ? new toastui.Editor.factory(editorConfig)
            : new toastui.Editor(editorConfig);

        $node.data("data-toast-editor", editor);
    });
}
// 토스트 에디터 - 에디터 초기화 - 끝

// 토스트 에디터 실행
ToastEditor__init();

// 토스트 에디터 끝