window.onload = function () {
    let text = document.getElementById('floatingTextarea2');
    let btn = document.getElementById('btnSend');
    let btnChange = document.getElementsByClassName('icon-pencil');

    btn.onclick = function () {
        return text.value.trim() !== '';
    }

    const myModal = document.getElementById('modal')
    myModal.addEventListener('show.bs.modal', event => {
        // Button that triggered the modal
        const button = event.relatedTarget
        let div = button.closest('div');
        let user_name_array = div.getElementsByClassName('from-username');
        let user_id_array = div.getElementsByClassName('user-id');
        let user_text_array = div.getElementsByClassName('user-text');
        // Extract info from data-bs-* attributes
        const user_id = user_id_array[0].innerText;
        const user_name = user_name_array[0].innerText;
        const user_text = user_text_array[0].innerText;
        // If necessary, you could initiate an AJAX request here
        // and then do the updating in a callback.
        //
        // Update the modal's content.
        const modalTitle = myModal.querySelector('#modalLabel')
        const modalId = myModal.querySelector('#modalId')
        const modalBodyInput = myModal.querySelector('.modal-body textarea')
        modalBodyInput.value = user_text
        modalTitle.textContent = user_name
        modalId.textContent = user_id

        const hiddenInputUsername = myModal.querySelector('#modal-username-hidden')
        hiddenInputUsername.value = user_name
        const hiddenInputId = myModal.querySelector('#modal-id-hidden')
        hiddenInputId.value = user_id

        // const temp = document.createElement('div')
        // temp.style.width = '450px'
        // temp.id = 'new-div'
        // myModal.appendChild(temp)
        // temp.innerText = user_text
        // temp.hidden = false;
        // console.log(modalBodyInput.value.split("\n").length)
        // modalBodyInput.style.height = Math.ceil(temp.style.height) + 'px'
        // myModal.handleUpdate()
    })

    myModal.addEventListener('loaded.bs.modal', event => {
        const modalBodyInput = myModal.querySelector('.modal-body textarea')
        auto_grow(modalBodyInput)
    })

    function auto_grow(textarea) {
        textarea.style.height = (textarea.scrollHeight)+"px";
    }
    // btnChange.onclick = function () {
    //     let userId = $(this).attr('user-id');
    //     $("#bookId").attr('value', userId);
    //     // let myBookId = "hui";
    //     // let modal = document.getElementById('bookId');
    //     // modal.innerText = myBookId;
    // }
}

// $('#my-modal').on('show.bs.modal', function(e) {
//
//     //get data-id attribute of the clicked element
//     let bookId = $(e.relatedTarget).data('user-id');
//
//     //populate the textbox
//     $(e.currentTarget).find('input[name="bookId"]').val(bookId);
// });

// $(document).on("click", ".icon-pencil", function () {
//     let myBookId = "hui";
//     let modal = document.getElementById('bookId');
//     modal.value = myBookId;
    // $("#bookId").val( myBookId );
    // As pointed out in comments,
    // it is unnecessary to have to manually call the modal.
    // $('#addBookDialog').modal('show');
// });