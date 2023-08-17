import styled from "styled-components";
import HeaderHome from "../components/Layout/HeaderHome";
import Hot10Box from "../components/Home/Hot10Box";
import SearchBox from "../components/Home/SearchBox";
import {
  motion,
  useTransform,
  useScroll,
  useAnimation,
  useInView,
} from "framer-motion";
import { useEffect, useRef, useState } from "react";
import { throttle } from "throttle-debounce-ts";
import Footer from "../components/Layout/Footer";
import { Link } from "react-router-dom";

const Container = styled.div`
  width: 100%;
  max-width: 100vw;
  min-height: 100vh;
  display: flex;
  flex-direction: column;

  background-color: white;
`;

// const HeroContainer = styled.div`
//   width: 100%;
//   max-width: 100%;
//   height: 600px;
//   background-image: url("/main.png"); /* Replace with the URL of your image */
//   background-size: cover;
//   background-position: center;

//   display: flex;
//   flex-direction: column;
//   justify-content: center;
//   align-items: center;
// `;

const HeroContainer = styled(motion.div)`
  width: 100%;
  max-width: 100%;
  height: 800px;
  /* height: 100vh; */

  background-image: url("/main.png"); /* Replace with the URL of your image */
  background-size: cover;
  background-position: center;

  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
`;

const InnerContainer = styled.div`
  margin-top: 130px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
`;

const Hot10Container = styled.div`
  margin-top: 30px;
`;

const BodyContainer = styled.div`
  /* width: 1200px; */
  width: 100vw;
  height: 100%;
  background-color: white;
  height: 100vh;
  /* margin: 0 auto; */

  display: flex;
  z-index: 9;
  overflow: hidden;

  overflow-x: hidden;

  /* flex-direction: column; */
`;

const TitleContainer = styled(motion.div)`
  font-weight: 700;
  font-size: 52px;
  display: flex;
  justify-content: flex-start;
  align-items: center;
  margin: 70px;
  margin-left: 150px;
  line-height: 150%;

  /* opacity: 0;
  y: 20px; */
`;

const CardContainer = styled.div`
  width: 550px;
  height: 450px;
  margin: 30px 30px;
  display: flex;
  flex-wrap: wrap;
  border-radius: 50px;

  /* Add styles for the content */
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  text-align: center;
  color: #fff;
  font-size: 32px;
  font-weight: 700;
  padding: 20px;

  /* Add a separate element for the background image */
  position: relative;
  overflow: hidden;

  /* overflow-x: scroll; */

  /* Apply the filter property to the background image element */
  &::before {
    content: "";
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background-image: ${(props) =>
      `url(${bgData[props.index]})`}; /* Set the background image from bgData */
    background-size: 125%; /* Set the background image to cover 90% of the container */
    background-repeat: no-repeat; /* Prevent image repetition */
    background-position: center; /* Center the background image */
    filter: blur(3px); /* Apply the filter to the background image */
    z-index: -1; /* Move the background image behind the content */
  }

  &:hover {
    transform: scale(1.02);
  }

  cursor: pointer;
`;

const StyledLink = styled(Link)`
  text-decoration: none; /* Remove underline */
  color: inherit; /* Inherit text color from parent */
`;

const bgData = [
  "main/1.png",
  "main/2.png",
  "main/3.png",
  "main/4.png",
  "main/5.png",
];

const mentData = [
  "스터디 검색하기",
  "원하는 스터디 신청하기",
  "스터디 만들기",
  "화상 스터디",
  "커뮤니티에서 정보 공유하기",
];

const urlMain = ["/search", "/search", "/makestudy", "/search", "/board/free"];

//메인 화면 애니메이션
const heroContainerAnimation = {
  initial: { opacity: 0 }, // Start with opacity 0 (dark)
  animate: { opacity: 1 }, // Animate to opacity 1 (bright)
  transition: { duration: 5 }, // Animation duration in seconds
};

function useElementViewportPosition(ref) {
  // ref 넣으면 위치를 반환해줌.
  const [position, setPosition] = useState([0, 0]);

  useEffect(() => {
    if (!ref || !ref.current) return;

    const pageHeight = document.body.scrollHeight;
    const start = ref.current.offsetTop;
    const end = start + ref.current.offsetHeight;

    setPosition([start / pageHeight, end / pageHeight]);
  }, []);

  return { position };
}

//framer motion 애니메이션 정의
const slideAnimation = {
  variants: {
    full: { backgroundColor: "#663399" },
    partial: { backgroundColor: "#808080" },
  },
  initial: "partial",
  whileinview: "full",
  viewport: { amount: 1, once: true },
  //  whileInView는 애니메이션 동작을 지정합니다. CardContainer가 뷰포트에 들어왔을 때, "full" 상태로 애니메이션이 작동하도록 합니다.
};

export default function Home() {
  const ref = useRef(null);
  const carouselRef = useRef(null);
  const { position } = useElementViewportPosition(ref);
  //useElementViewportPosition 커스텀 훅을 사용하여 ref로 참조된 DOM 요소가 뷰포트 내에서 어느 정도 보이는지에 대한 정보를 position 변수로 받아옵니다.
  const [carouselEndPosition, setCarouselEndPosition] = useState(0);
  const { scrollYProgress } = useScroll();
  //  useScroll 훅을 사용하여 현재 스크롤 위치에 따른 스크롤 진행 상태를 scrollYProgress 변수로 받아옵니다.

  const x = useTransform(scrollYProgress, position, [0, carouselEndPosition]);

  // useEffect(() => {
  //   window.addEventListener("scroll", () =>
  //     console.log({ scrollYProgress: scrollYProgress.current })
  //   );
  // }, []);

  useEffect(() => {
    if (!carouselRef || !carouselRef.current) return;
    const parent = carouselRef.current.parentElement;
    const scrollbarWidth =
      window.innerWidth - document.documentElement.clientWidth;

    const resetCarouselEndPosition = () => {
      if (carouselRef && carouselRef.current) {
        const newPosition =
          carouselRef.current.clientWidth -
          window.innerWidth +
          scrollbarWidth +
          parent.offsetLeft * 2;

        setCarouselEndPosition(-newPosition);
      }
    };

    resetCarouselEndPosition();
    const handleResize = throttle(10, resetCarouselEndPosition);

    window.addEventListener("resize", handleResize);
    return () => window.removeEventListener("resize", handleResize);
  }, []);

  //타이틀 애니메이션
  const controls = useAnimation();
  const { refTitle, inView } = useInView({ triggerOnce: true });

  useEffect(() => {
    if (inView) {
      controls.start({ opacity: 1, y: 0 });
    }
  }, [controls, inView]);

  return (
    <Container>
      <HeaderHome />

      <HeroContainer
        variants={heroContainerAnimation}
        initial="initial"
        animate="animate"
      >
        <InnerContainer>
          <SearchBox width={750} />
          <Hot10Container>
            <Hot10Box />
          </Hot10Container>
        </InnerContainer>
      </HeroContainer>

      {/* <HeroContainer>
        <InnerContainer>
          <SearchBox />
          <Hot10Container>
            <Hot10Box />
          </Hot10Container>
        </InnerContainer>
      </HeroContainer> */}

      {/* <section
        className="slide fade-6 kenBurns"
        style={{ width: "1000px", height: "1000px" }}
      >
        <div className="content">
          <div className="container">
            <div className="wrap">
              <div className="fix-3-12">
                <svg height="77" className="wide ae-1 fromCenter">
                  <use href="assets/svg/icons.svg#logo"></use>
                </svg>
              </div>
            </div>
          </div>
        </div>
        <div
          className="background"
          style={{ backgroundImage: "url(/main.png)" }}
        ></div>
      </section> */}

      {/* 슬라이드뷰 */}
      {/* <Carousel cycleNavigation={true} navButtonsAlwaysVisible={true}>
        {stepFourCarousel.map((content) => (
          <>
            <Typography variant="h3" color="#112b23">
              {content.title}
            </Typography>
            <img src={content.url} />
            {content.desc.map((description) => (
              <li>{description}</li>
            ))}
          </>
        ))}
      </Carousel> */}

      <TitleContainer>
        뷰잉, <br /> 이렇게 사용하세요.
      </TitleContainer>

      <BodyContainer>
        <section ref={ref}>
          <div className="container" style={{ height: "300vh" }}>
            <div className="sticky-wrapper">
              <motion.div
                ref={carouselRef}
                className="carousel"
                style={{ x, display: "flex", overflow: "hidden" }}
              >
                {Array.from(Array(5).keys()).map((i) => (
                  <StyledLink to={urlMain[i]} key={i}>
                    <CardContainer
                      {...slideAnimation}
                      key={i}
                      className="carousel__slide"
                      index={i}
                    >
                      {/* {i + 1} */}
                      {/* 원하는 내용 */}
                      {mentData[i]} {/* Display the content from mentData */}
                    </CardContainer>
                  </StyledLink>
                  // <motion.div
                  //   {...slideAnimation}
                  //   key={i}
                  //   className="carousel__slide"
                  // >
                  //   {i + 1}
                  // </motion.div>
                ))}
              </motion.div>
            </div>
          </div>
        </section>
      </BodyContainer>
      <Footer />
    </Container>
  );
}
