import {classNames} from "react-ui-basics/Tools";
import FormUploadProgress from "react-ui-basics/FormUploadProgress";
import DropFileInput from "react-ui-basics/DropFileInput";
import {css} from "goober";
import * as API from "../services/NetworkService";
import {useState} from "react";

const styles = css`
  &.App {
    min-height: 100vh;
    padding: 50px;

    .Dropzone {
      min-height: 200px;
    }
  }
`

const saveFile = (blob: Blob, filename: string) => {
    const blobURL = window.URL.createObjectURL(blob);
    const link = document.createElement('a');
    link.href = blobURL;
    link.download = filename;
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
};

export default () => {
    const [total, setTotal] = useState(0)
    const [uploaded, setUploaded] = useState(0)
    const [progress, setProgress] = useState(0)
    const [cancel, setCancel] = useState<() => void>()


    return <div className={classNames("App", styles)}>
        <DropFileInput
            multiple={false}
            icon={'upload'}
            label={'Choose image to unnotch!'}
            accept={"image/*"}
            onDrop={async files => {
                console.log(files)
                setUploaded(0)
                setProgress(0)
                const file = files[0];
                setTotal(file.size)
                try {
                    const response = await API.upload({
                        file: file,
                        name: file.name,
                        width: 3024,
                        height: 1964,
                    }, (e) => {
                        setUploaded(e.loaded)
                        setTotal(e.total)
                        setProgress(e.loaded / e.total * 100)
                    }, cancel => setCancel(prevState => () => {
                        cancel()
                        setTotal(0)
                    }))
                    saveFile(response, file.name)
                } finally {
                    setTotal(0)
                }
            }}
        />
        {!!total && <FormUploadProgress
            processingLabel={'Processing..'}
            cancelLabel={'Cancel'}
            value={progress}
            loaded={uploaded}
            total={total}
            cancel={cancel}
        />}
    </div>
}