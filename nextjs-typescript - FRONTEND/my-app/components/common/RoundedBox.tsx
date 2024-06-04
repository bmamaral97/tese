import { Box, useColorModeValue } from '@chakra-ui/react'

interface RoundedBoxProps {
    children: React.ReactNode
}

function RoundedBox({ children }: RoundedBoxProps) {
    return (
        <Box
            p={8}
            rounded='xl'
            bg={useColorModeValue('gray.100', 'gray.900')}
            boxShadow='inner'
            mb={12}
        >
            {children}
        </Box>
    )
}

export default RoundedBox