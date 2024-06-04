import { Center, Container, Spinner } from "@chakra-ui/react";

function Loading() {
  return (
    <Container display="flex" justifyContent="center" alignItems="center">
      <Center height="50vh">
        <Spinner />
      </Center>
    </Container>
  );
}

export default Loading;
