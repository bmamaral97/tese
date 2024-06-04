import { Box, useColorModeValue } from '@chakra-ui/react'

interface RoundedBoxClearProps {
    children: React.ReactNode
}

function RoundedBoxClear({ children }: RoundedBoxClearProps) {
    return (
        <Box
            boxShadow='xs'
            p={8}
            rounded='xl'
            bg='white'
            minWidth={"200px"}
        >
            {children}
        </Box>
    )
}

export default RoundedBoxClear