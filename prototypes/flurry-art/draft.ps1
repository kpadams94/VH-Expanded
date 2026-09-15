Add-Type -AssemblyName System.Drawing
$root=$PSScriptRoot
function Put($im,$x,$y,$hex){if($x -ge 0 -and $x -lt 16 -and $y -ge 0 -and $y -lt 16){$im.SetPixel($x,$y,[Drawing.ColorTranslator]::FromHtml($hex))}}
for($v=1;$v -le 3;$v++){
$legs=@(
,@(@(1,14),@(2,13),@(3,13),@(4,12),@(5,12),@(6,11),@(7,11),@(8,11),@(9,11),@(10,11),@(11,11),@(12,11)),
,@(@(12,11),@(11,10),@(10,9),@(9,9),@(8,8),@(7,8),@(6,8),@(5,8),@(4,8),@(3,8)),
,@(@(3,8),@(4,7),@(5,6),@(6,6),@(7,5),@(8,5),@(9,5),@(10,5),@(11,5),@(12,5)),
,@(@(12,5),@(11,4),@(10,3),@(9,3),@(8,2),@(7,2),@(6,1),@(5,1),@(4,1),@(3,1)))
if($v -eq 2){$legs[0]=,@(@(1,14),@(2,14),@(3,13),@(4,13),@(5,12),@(6,12),@(7,11),@(8,11),@(9,11),@(10,11),@(11,11),@(12,11))}
if($v -eq 3){$legs[1]=,@(@(12,11),@(11,10),@(10,10),@(9,9),@(8,9),@(7,8),@(6,8),@(5,8),@(4,8),@(3,8))}
$im=[Drawing.Bitmap]::new(16,16);$all=@();foreach($leg in $legs){foreach($p in $leg[0]){$all+=,@($p)}}
foreach($p in $all){foreach($d in @(@(-1,0),@(1,0),@(0,-1),@(0,1))){Put $im ($p[0]+$d[0]) ($p[1]+$d[1]) '#4B2519'}}
$palette=@('#FFDF79','#F58A32','#F95449','#D92A43')
for($i=0;$i -lt 4;$i++){foreach($p in $legs[$i][0]){Put $im $p[0] $p[1] $palette[$i]}}
$hits=@(@(12,11),@(3,8),@(12,5),@(3,1))
for($i=0;$i -lt 4;$i++){$p=$hits[$i];if($v -ne 2){foreach($d in @(@(-1,0),@(1,0),@(0,-1),@(0,1))){Put $im ($p[0]+$d[0]) ($p[1]+$d[1]) $palette[$i]}};Put $im $p[0] $p[1] '#FFF2BC'}
$im.Save((Join-Path $root "native/0$v-color.png"));$sil=[Drawing.Bitmap]::new($im);for($y=0;$y -lt 16;$y++){for($x=0;$x -lt 16;$x++){if($sil.GetPixel($x,$y).A -gt 0){Put $sil $x $y '#FFF2BC'}}};$sil.Save((Join-Path $root "native/0$v.png"));$sil.Dispose();$im.Dispose()
}
$b=[Drawing.Bitmap]::new(720,310);$g=[Drawing.Graphics]::FromImage($b);$g.Clear([Drawing.ColorTranslator]::FromHtml('#191C22'));$g.InterpolationMode='NearestNeighbor';$g.PixelOffsetMode='Half';$f=[Drawing.Font]::new('Segoe UI',11)
for($v=1;$v -le 3;$v++){$im=[Drawing.Bitmap]::new((Join-Path $root "native/0$v.png"));$x=20+240*($v-1);$g.DrawString("0$v",$f,[Drawing.Brushes]::White,$x,10);$g.DrawImage($im,[Drawing.Rectangle]::new($x,40,16,16));$g.DrawImage($im,[Drawing.Rectangle]::new($x+45,35,48,48));$g.DrawImage($im,[Drawing.Rectangle]::new($x,95,192,192));$im.Dispose()};$g.Dispose();$b.Save((Join-Path $root 'silhouettes.png'));$b.Dispose()
