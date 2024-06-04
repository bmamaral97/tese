import { ModalOverlay, ModalContent, ModalHeader, ModalCloseButton, ModalBody, Modal, Box } from "@chakra-ui/react"

interface BaseModalProps {
    isOpen: boolean,
    onClose: () => void,
    title: string,
    body: string | JSX.Element,
}

function BaseModal({ isOpen, onClose, title, body }: BaseModalProps) {
    return (
        <Box>
            <Modal isOpen={isOpen} onClose={onClose} isCentered size={'xl'}>
                <ModalOverlay />
                <ModalContent>
                    <ModalHeader>{title}</ModalHeader>
                    <ModalCloseButton />
                    <ModalBody>{body}</ModalBody>
                </ModalContent>
            </Modal>
        </Box>
    )
}

export default BaseModal