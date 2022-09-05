function addProductItem(colors) {
    var table = document.getElementById('table_items')
    var row = table.insertRow()
    var cell = row.insertCell()
    var select = document.createElement('select')
    select.setAttribute('class', 'form-control')
    select.setAttribute('name', 'color_id[]')
    var defaultOption = document.createElement('option')
    defaultOption.setAttribute('value', '')
    defaultOption.innerHTML = 'select color...'
    select.appendChild(defaultOption)
    for (var i = 0; i < colors.length; i++) {
        var color = colors[i]
        var option = document.createElement('option')
        option.setAttribute('value', color['id'])
        option.innerHTML = color['ui_name']
        select.appendChild(option)
    }
    // TODO: add option to create new color
    cell.appendChild(select)
}