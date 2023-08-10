import React, { useState } from "react";
import styled, { css } from "styled-components";
import SubButton from "./../Button/SubButton";

// Color options
const colors = [
  "#ff6767", // Red
  "#a5ffa5", // Green
  "#a9a9ff", // Blue
  "#ff76f6", // Magenta
  "#ffff9e", // Yellow
  "#93ffff", // Cyan
  "#9c209c", // Purple
  "#ffa600", // Orange
  "#058e00", // Dark Green
  "#ff00ee", // Orange Red
];

// PNG files
const images = [
  "cow",
  "crab",
  "dolphin",
  "jellyfish",
  "koala",
  "octopus",
  "penguin",
  "seahorse",
  "sheep",
  "turtle",
  // Add the paths to your 10 PNG files here
];

const Container = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  flex-wrap: wrap;
`;

const ColorContainer = styled.div`
  /* background-color: var(--gray-200); */
  display: flex;
`;

const ColorOption = styled.div`
  width: 30px;
  height: 30px;
  display: inline;
  margin: 5px;
  border-radius: 50%;

  border: 2px solid
    ${({ $isselected }) => ($isselected ? "var(--gray-200)" : "white")};
  box-shadow: 2px 2px 2px 0px rgba(123, 123, 123, 0.5);

  background-color: white;

  cursor: pointer;
`;

const ImageContainer = styled.div`
  /* background-color: var(--gray-200); */
  display: flex;
`;

const ImageOption = styled.div`
  width: 60px;
  height: 60px;
  /* margin: 5px; */
  cursor: pointer;
  display: inline;

  border: 0.5px var(--gray-200) solid;

  background-image: ${({ $selectedimage }) => `url(/profile/${$selectedimage}.png)`};

  background-size: 65%; /* Set the background image to cover 90% of the container */
  background-repeat: no-repeat; /* Prevent image repetition */
  background-position: center; /* Center the background image */

  background-color: ${({ $isselected }) =>
    $isselected ? "var(--gray-200)" : "var(--gray-50)"};

  /* border-radius: 50%; */

  /* border: 2px white solid;
  box-shadow: 2px 2px 2px 0px rgba(123, 123, 123, 0.5); */

  /* background-color: black; */
`;

const SelectedContainer = styled.div`
  width: 70px;
  height: 70px;
  /* border: 2px solid #000; */
  position: relative;

  padding: 10px;
  margin-top: 20px;
  border-radius: 50%;
  background-image: ${({ $selectedimage }) => `url(${$selectedimage})`};

  background-color: ${({ $selectedcolor }) => $selectedcolor};

  background-size: 65%; /* Set the background image to cover 90% of the container */
  background-repeat: no-repeat; /* Prevent image repetition */
  background-position: center; /* Center the background image */
`;

export default function SetProfileImage({selectedColor, setSelectedColor, selectedImage, setSelectedImage}) {

  const handleReset = () => {
    setSelectedColor("var(--gray-300)");
    setSelectedImage("default");
  };

  const handleColorClick = (color) => {
    setSelectedColor(color);
    // setSelectedImage(""); // Clear the selected image when a color is selected
  };

  const handleImageClick = (image) => {
    setSelectedImage(image);
    // setSelectedColor(""); // Clear the selected color when an image is selected
  };

  return (
    <Container>
      {/* Render color options */}

      <SelectedContainer
        $selectedcolor={selectedColor}
        $selectedimage={`/profile/${selectedImage}.png`}
      ></SelectedContainer>
      <br></br>

      <SubButton content="초기화" onClick={() => handleReset()} />

      <br></br>
      <ImageContainer>
        
        {/* Render image options */}
        {images.map((image, index) => (
          <ImageOption
            key={index}
            $selectedimage={image}
            alt={`Image ${index + 1}`}
            onClick={() => handleImageClick(image)}
            $isselected={selectedImage === image} // Updated this line
          />
        ))}
      </ImageContainer>
      <br></br>
      <ColorContainer>
        {colors.map((color, index) => (
          <ColorOption
            key={index}
            style={{ backgroundColor: color }}
            onClick={() => handleColorClick(color)}
            $isselected={selectedColor === color}
          />
        ))}
      </ColorContainer>
    </Container>
  );
}
