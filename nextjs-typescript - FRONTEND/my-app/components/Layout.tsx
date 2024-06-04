import React from "react";
import Navbar from "./Navbar";
import { Box } from "@chakra-ui/react";
import { Footer } from "./Footer";
import { useRouter } from "next/router";

type Props = {
  children?: React.ReactNode;
};

const Layout = ({ children }: Props) => {
  const router = useRouter();
  return (
    <Box
      display={"flex"}
      minHeight={"100vh"}
      flexDirection={"column"}
      justifyContent={"flex-start"}
    >
      <Navbar />

      {children}

      {router.asPath !== "/login" ? (
        <Box marginTop={"auto"}>
          <Footer />
        </Box>
      ) : null}
    </Box>
  );
};

export default Layout;
