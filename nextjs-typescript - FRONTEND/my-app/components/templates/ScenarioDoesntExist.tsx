import { Box, Center, Container, Text } from "@chakra-ui/react"

function ScenarioDoesntExist() {

    return (

        <Box>
            <Container maxW={'container.lg'} centerContent>
                <Center h={"50vh"}>
                    <Text fontSize='50px'>The current scenario you are trying to access does not exist...</Text>
                </Center>
            </Container>
        </Box>

    )

}

export default ScenarioDoesntExist