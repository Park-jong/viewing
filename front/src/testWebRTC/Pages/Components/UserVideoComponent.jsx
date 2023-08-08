import React from "react";
import OpenViduVideoComponent from "./OvVideo";

const UserVideoComponent = (props) => {
  const getNicknameTag = () => {
    return props.streamManager.stream.connection.data;
  };

  return (
    <div>
      {props.streamManager !== undefined ? (
        <div className="streamcomponent">
          {!!props.streamManager.stream.videoActive ? (
            <OpenViduVideoComponent streamManager={props.streamManager} />
          ) : (
            <img src="test.jpg" alt="profileImage"></img>
          )}

          <div>
            <p>userData: {getNicknameTag()}</p>
          </div>
        </div>
      ) : null}
    </div>
  );
};

export default UserVideoComponent;
