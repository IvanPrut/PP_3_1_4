let roleList = [
    {id: 1, role: "USER"},
    {id: 2, role: "ADMIN"}
]

let isUser = true;

const csrfToken = document.querySelector('meta[name="csrf-token"]').getAttribute('content');

$(async function () {
    await getUser();
    await infoUser();
    await tittle();
    await getUsers();
    await getNewUserForm();
    await getDefaultModal();
    await createUser();
})

const userFetch = {
    head: {
        'Accept': 'application/json',
        'Content-Type': 'application/json',
        'Referer': null,
        'X-CSRF-TOKEN': csrfToken,
    },
    findAllUsers: async () => await fetch('api/admin/users'),
    findUserByUsername: async () => await fetch(`api/user`),
    findOneUser: async (id) => await fetch(`api/admin/users/${id}`),
    addNewUser: async (user) => await fetch('api/admin/users', {
        method: 'POST',
        headers: userFetch.head,
        body: JSON.stringify(user)
    }),
    updateUser: async (user, id) => await fetch(`api/admin/users/${id}`, {
        method: 'PUT',
        headers: userFetch.head,
        body: JSON.stringify(user)
    }),
    deleteUser: async (id) => await fetch(`api/admin/users/${id}`, {method: 'DELETE',
        headers: userFetch.head})
}

async function infoUser() {
    let temp = '';
    const info = document.querySelector('#info');
    await userFetch.findUserByUsername()
        .then(res => res.json())
        .then(user => {
            temp += `
             <b>
                <span className="ps-2">
                    ${user.email}
                </span>
             </b>
             <span> with roles: ${user.roles.map(e => " " + e.roleName)}</span>
            `;
        });
    info.innerHTML = temp;
}

