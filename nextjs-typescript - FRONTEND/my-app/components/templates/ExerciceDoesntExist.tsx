import { Box, Center, Container, Text } from "@chakra-ui/react"

function ExerciseDoesntExist() {

    return (

        <Box>
            <Container maxW={'container.lg'} centerContent>
                <Center h={"50vh"}>
                    <Text fontSize='50px'>The current exercise you are trying to access does not exist...</Text>
                </Center>
            </Container>
        </Box>

    )

}

export default ExerciseDoesntExist