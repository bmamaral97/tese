import { Container } from '@chakra-ui/react'

interface RoundedBoxProps {
    children: React.ReactNode
}

function DefaultContainer({ children }: RoundedBoxProps) {
    return (
        <Container
            maxW={'container.md'}
            centerContent
            background={"white"}
            //border='2px'
            rounded={'lg'}
        >
            {children}
        </Container>
    )
}

export default DefaultContainer