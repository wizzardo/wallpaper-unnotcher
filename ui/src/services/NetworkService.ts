import {fetch} from "react-ui-basics/HttpTools";

const base = ''

export const upload = (
    params,
    onProgress?: (ev: ProgressEvent) => any,
    provideCancel?: ((cancelFunction: () => void) => void),
) => fetch<Blob>(base + '/upload', {
    params,
    method: "POST",
    multipart: true,
    responseType: "blob",
    onProgress,
    provideCancel,
})