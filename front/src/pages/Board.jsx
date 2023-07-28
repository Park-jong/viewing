import { useDispatch, useSelector} from "react-redux"
import { ChangeToggle } from "../modules/UserRedux/Actions";

export default function Board() {

  const dispatch = useDispatch()
  const user = useSelector((state) => state.UserReducer)
  console.log(user.toggle)
  return <>
    <button onClick={() => dispatch(ChangeToggle())}>Toggle</button>    
  </>;
}
