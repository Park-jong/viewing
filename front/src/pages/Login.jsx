import DownloadFiles from "../components/Common/UploadFile";
import InputBox from "../components/Common/InputBox";
import Toggle from "../components/Common/Toggle";
import ApplyModal from "../components/Modal/ApplyModal";

export default function Login() {
  return (
    <>
      <ApplyModal isOpen={false}></ApplyModal>
      <Toggle></Toggle>
    </>
  );
}
