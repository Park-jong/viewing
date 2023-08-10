import styled from "styled-components";

const CompanyContainer = styled.div`
  display: flex;
  /* margin-top: 30px; */
`;

const CompanyTag = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;

  width: fit-content;
  height: 25px;
  background-color: rgb(
    145,
    118,
    255
  ); /* Change the alpha value to adjust opacity */

  margin: 5px;
  padding: 0 5px;

  border-radius: 5px;
`;

const PositionTag = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;

  width: fit-content;
  height: 25px;

  background-color: var(--secondary);

  margin: 5px;
  padding: 0 5px;

  border-radius: 5px;
`;

const CareerTag = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
  width: fit-content;
  height: 25px;
  background-color: var(--secondary);

  margin: 5px;
  padding: 0 5px;

  border-radius: 5px;
`;

export default function CompanyJobTag({ company, position, career }) {
  return (
    <CompanyContainer>
      <CompanyTag>{company}</CompanyTag>
      <PositionTag>{position}</PositionTag>
      <CareerTag>{career}</CareerTag>
    </CompanyContainer>
  );
}
