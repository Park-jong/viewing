import React, { useEffect, useRef } from "react";

const OvVideo = (props) => {

    const videoRef = useRef();

    useEffect(() => {
        if (props && !!videoRef) {
            props.streamManager.addVideoElement(videoRef.current);
        }
    });

    return (
        <>
        <video autoPlay={true} ref={videoRef} />
        </>
    );

}

export default OvVideo;