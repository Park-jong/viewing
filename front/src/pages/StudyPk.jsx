import { useOutletContext } from "react-router-dom";
import StudyPkAdmin from "./StudyPkAdmin";
import StudyPkInfo from "./StudyPkInfo";

export default function StudyPk() {
  const { isLeader } = useOutletContext();

  return <>{isLeader ? <StudyPkAdmin /> : <StudyPkInfo />}</>;
}
