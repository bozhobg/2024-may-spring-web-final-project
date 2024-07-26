window.addEventListener('load', loadSelector);

async function loadSelector() {

    const BASE_URL = 'http://localhost:8080/api/ingredients/short';

    const availableIngSelect = document.getElementById('availableIngredients');
    const chosenIngSelect = document.getElementById('ingredients')
    const filterInput = document.getElementById('ingInputFilter')
    const filterBtn = document.getElementById('filterBtn');
    const addBtn = document.getElementById('addBtn');
    const removeBtn = document.getElementById('removeBtn');
    const confirmBtn = document.getElementById('confirmBtn')

    clearChosen();
    let available = await loadAvailableIngredients();

    filterBtn.addEventListener('click', handleFilter);
    addBtn.addEventListener('click', handleAdd);
    removeBtn.addEventListener('click', handleRemove)
    confirmBtn.addEventListener('click', handleConfirm)

    function handleAdd(e) {
        e.preventDefault();

         let selected = document.querySelectorAll('#availableIngredients option:checked');
         selected.forEach(opt => {
             opt.remove();
             chosenIngSelect.appendChild(opt);
             available = available.filter(i => i.id != opt.value);
         })
    }

    function handleRemove(e) {
        e.preventDefault();

        let selected = document.querySelectorAll('#ingredients option:checked');
        selected.forEach(opt => {
            opt.remove();
            availableIngSelect.appendChild(opt);
            available.push({id: opt.value, name: opt.textContent});
        })
    }

    function handleConfirm(e) {
        e.preventDefault();

        chosenIngSelect.querySelectorAll('option')
            .forEach(opt => opt.selected = true);
        chosenIngSelect.setAttribute('disabled', 'true');

        document.querySelector('div.available-container').remove();
        removeBtn.remove();
        confirmBtn.remove();
    }

    function clearChosen() {
        chosenIngSelect.innerHTML = '';
    }

    function handleFilter(e) {

        e.preventDefault();

        const filterValue = filterInput.value;

        availableIngSelect.innerHTML = '';

        if (filterValue === null || filterValue === '') {
            available.forEach(i => availableIngSelect.appendChild(createOption(i)));
            return;
        }

        let result = available.filter(i => i.name.toLowerCase().includes(filterValue.toLowerCase()));
        result.forEach(i => availableIngSelect.appendChild(createOption(i)));
    }

    async function loadAvailableIngredients() {
        availableIngSelect.innerHTML = '';

        const res = await fetch(BASE_URL);
        const data = await res.json();

        let available = [];

        data.forEach(i => {
            available.push(i);
            availableIngSelect.appendChild(createOption(i));
        });

        return available;
    }

    function createOption(ingData) {
        let opt = document.createElement('option');
        opt.value = ingData.id;
        opt.textContent = ingData.name

        return opt;
    }
}